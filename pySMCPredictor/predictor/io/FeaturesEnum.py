'''
Created on Aug 30, 2016
@author: Mehmet Emin BAKIR
'''
from enum import Enum
# Enum Features class
class FeaturesEnum(Enum):
    ''' The Features which are used for prediction '''
    _features = None

    '''Experiment results shows that, Reciprocity, Weak Component, biconnected Components and Articulation Points
    # does not adds much to accuracy but they are computationally very expensive,
    # Hence we removed them from following DP properties, second and third DP still has these features.
    # Instead we added SUM of degrees, which higher rates and much more faster to compute.'''

    DP = ['SpeciesG_PD_Vertices',
    'SpeciesG_PD_Edges', 'SpeciesG_PD_InDegreeMIN', 'SpeciesG_PD_InDegreeMEAN',
    'SpeciesG_PD_InDegreeMAX', 'SpeciesG_PD_InDegreeSUM', 'SpeciesG_PD_OutDegreeMIN',
    'SpeciesG_PD_OutDegreeMEAN', 'SpeciesG_PD_OutDegreeMAX', 'SpeciesG_PD_OutDegreeSUM',
    'SpeciesG_PD_TotalDegreeMIN', 'SpeciesG_PD_TotalDegreeMEAN',
    'SpeciesG_PD_TotalDegreeMAX', 'SpeciesG_PD_TotalDegreeSUM', 'SpeciesG_PD_Density', \
    # 'SpeciesG_PD_Reciprocity', 'SpeciesG_PD_WeakComponents',
    # 'SpeciesG_PUD_UNDIRECTEDBiconnectedComponents',
    # 'SpeciesG_PUD_UNDIRECTEDArticulationPoints',
    'ReactionG_PD_Vertices', 'ReactionG_PD_Edges',
    'ReactionG_PD_InDegreeMIN', 'ReactionG_PD_InDegreeMEAN',
    'ReactionG_PD_InDegreeMAX', 'ReactionG_PD_InDegreeSUM', 'ReactionG_PD_OutDegreeMIN',
    'ReactionG_PD_OutDegreeMEAN', 'ReactionG_PD_OutDegreeMAX', 'ReactionG_PD_OutDegreeSUM',
    'ReactionG_PD_TotalDegreeMIN', 'ReactionG_PD_TotalDegreeMEAN',
    'ReactionG_PD_TotalDegreeMAX', 'ReactionG_PD_TotalDegreeSUM', 'ReactionG_PD_Density'\
    # 'ReactionG_PD_Reciprocity', 'ReactionG_PD_WeakComponents',
    # 'ReactionG_PUD_UNDIRECTEDBiconnectedComponents',
    # 'ReactionG_PUD_UNDIRECTEDArticulationPoints'
    ]

    # Difference between SSA props are only addition of SUM properties
    DPwithSUM = ['SpeciesG_PD_Vertices',  # DPwithSUM
    'SpeciesG_PD_Edges', 'SpeciesG_PD_InDegreeMIN', 'SpeciesG_PD_InDegreeMEAN',
    'SpeciesG_PD_InDegreeMAX', 'SpeciesG_PD_InDegreeSUM', 'SpeciesG_PD_OutDegreeMIN',
    'SpeciesG_PD_OutDegreeMEAN', 'SpeciesG_PD_OutDegreeMAX', 'SpeciesG_PD_OutDegreeSUM',
    'SpeciesG_PD_TotalDegreeMIN', 'SpeciesG_PD_TotalDegreeMEAN',
    'SpeciesG_PD_TotalDegreeMAX', 'SpeciesG_PD_TotalDegreeSUM', 'SpeciesG_PD_Density',
    'SpeciesG_PD_Reciprocity', 'SpeciesG_PD_WeakComponents',
    'SpeciesG_PUD_UNDIRECTEDBiconnectedComponents',
    'SpeciesG_PUD_UNDIRECTEDArticulationPoints', 'ReactionG_PD_Vertices', 'ReactionG_PD_Edges',
    'ReactionG_PD_InDegreeMIN', 'ReactionG_PD_InDegreeMEAN',
    'ReactionG_PD_InDegreeMAX', 'ReactionG_PD_InDegreeSUM', 'ReactionG_PD_OutDegreeMIN',
    'ReactionG_PD_OutDegreeMEAN', 'ReactionG_PD_OutDegreeMAX', 'ReactionG_PD_OutDegreeSUM',
    'ReactionG_PD_TotalDegreeMIN', 'ReactionG_PD_TotalDegreeMEAN',
    'ReactionG_PD_TotalDegreeMAX', 'ReactionG_PD_TotalDegreeSUM', 'ReactionG_PD_Density',
    'ReactionG_PD_Reciprocity', 'ReactionG_PD_WeakComponents',
    'ReactionG_PUD_UNDIRECTEDBiconnectedComponents',
    'ReactionG_PUD_UNDIRECTEDArticulationPoints']

    # Same features as SSA
    DPwithOutSum = ['SpeciesG_PD_Vertices',
    'SpeciesG_PD_Edges', 'SpeciesG_PD_InDegreeMIN', 'SpeciesG_PD_InDegreeMEAN',
    'SpeciesG_PD_InDegreeMAX', 'SpeciesG_PD_OutDegreeMIN',
    'SpeciesG_PD_OutDegreeMEAN', 'SpeciesG_PD_OutDegreeMAX',
    'SpeciesG_PD_TotalDegreeMIN', 'SpeciesG_PD_TotalDegreeMEAN',
    'SpeciesG_PD_TotalDegreeMAX', 'SpeciesG_PD_Density',
    'SpeciesG_PD_Reciprocity', 'SpeciesG_PD_WeakComponents',
    'SpeciesG_PUD_UNDIRECTEDBiconnectedComponents',
    'SpeciesG_PUD_UNDIRECTEDArticulationPoints', 'ReactionG_PD_Vertices', 'ReactionG_PD_Edges',
    'ReactionG_PD_InDegreeMIN', 'ReactionG_PD_InDegreeMEAN',
    'ReactionG_PD_InDegreeMAX', 'ReactionG_PD_OutDegreeMIN',
    'ReactionG_PD_OutDegreeMEAN', 'ReactionG_PD_OutDegreeMAX',
    'ReactionG_PD_TotalDegreeMIN', 'ReactionG_PD_TotalDegreeMEAN',
    'ReactionG_PD_Reciprocity', 'ReactionG_PD_WeakComponents',
    'ReactionG_PUD_UNDIRECTEDBiconnectedComponents',
    'ReactionG_PUD_UNDIRECTEDArticulationPoints']


    NG = ['NG_NumOfNONConstantSpecies', 'NG_Species_X_Reactions', 'NG_UpdatesMIN', 'NG_UpdatesMEAN',
    'NG_UpdatesMAX', 'NG_UpdatesSUM']  # NG_NumOfConstantSpecies

    def __init__(self, features):
        self._features = features

    @property
    def features(self):
        return self._features

